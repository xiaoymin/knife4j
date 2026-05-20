function warn(message, error) {
  if (window.console && typeof window.console.warn === 'function') {
    window.console.warn(message, error);
  }
}

function normalizeScriptUrls(scriptUrls) {
  if (!Array.isArray(scriptUrls)) {
    return [];
  }
  return scriptUrls
    .filter(scriptUrl => typeof scriptUrl === 'string' && scriptUrl.trim() !== '')
    .map(scriptUrl => scriptUrl.trim());
}

const existingApi = window.Knife4j || {};
const loadedScriptUrls = {};
const requestInterceptors = existingApi._requestInterceptors || [];

function registerRequestInterceptor(interceptor) {
  if (typeof interceptor !== 'function') {
    warn('Knife4j request interceptor must be a function.');
    return;
  }
  requestInterceptors.push(interceptor);
}

function applyRequestInterceptors(config, context) {
  let nextConfig = config || {};
  const nextContext = context || {};
  nextConfig.headers = nextConfig.headers || {};
  requestInterceptors.forEach(interceptor => {
    try {
      const returnedConfig = interceptor(nextConfig, nextContext);
      if (returnedConfig && typeof returnedConfig.then === 'function') {
        warn('Knife4j request interceptor must return config synchronously.');
      } else if (returnedConfig) {
        nextConfig = returnedConfig;
        nextConfig.headers = nextConfig.headers || {};
      }
    } catch (error) {
      warn('Knife4j request interceptor failed.', error);
    }
  });
  return nextConfig;
}

function loadScript(scriptUrl) {
  if (loadedScriptUrls[scriptUrl]) {
    return Promise.resolve();
  }
  loadedScriptUrls[scriptUrl] = true;
  return new Promise(resolve => {
    const script = document.createElement('script');
    script.src = scriptUrl;
    script.async = false;
    script.onload = () => resolve();
    script.onerror = error => {
      warn('Knife4j custom script load failed: ' + scriptUrl, error);
      resolve();
    };
    (document.head || document.body).appendChild(script);
  });
}

function loadCustomScripts(scriptUrls) {
  let chain = Promise.resolve();
  normalizeScriptUrls(scriptUrls).forEach(scriptUrl => {
    chain = chain.then(() => loadScript(scriptUrl));
  });
  return chain;
}

const knife4jExtension = Object.assign(existingApi, {
  registerRequestInterceptor,
  applyRequestInterceptors,
  loadCustomScripts,
  _requestInterceptors: requestInterceptors
});

window.Knife4j = knife4jExtension;

export default knife4jExtension;
export { registerRequestInterceptor, applyRequestInterceptors, loadCustomScripts };
