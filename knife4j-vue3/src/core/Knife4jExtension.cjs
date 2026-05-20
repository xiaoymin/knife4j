function defaultEnvironment() {
  var browserWindow = typeof window !== 'undefined' ? window : {};
  return {
    window: browserWindow,
    document: browserWindow.document || (typeof document !== 'undefined' ? document : null),
    console: browserWindow.console || (typeof console !== 'undefined' ? console : null)
  };
}

function normalizeScriptUrls(scriptUrls) {
  if (!Array.isArray(scriptUrls)) {
    return [];
  }
  return scriptUrls
    .filter(function (scriptUrl) {
      return typeof scriptUrl === 'string' && scriptUrl.trim() !== '';
    })
    .map(function (scriptUrl) {
      return scriptUrl.trim();
    });
}

function createKnife4jExtension(environment) {
  var env = Object.assign(defaultEnvironment(), environment || {});
  var loadedScriptUrls = {};
  var requestInterceptors = [];

  function warn(message, error) {
    if (env.console && typeof env.console.warn === 'function') {
      env.console.warn(message, error);
    }
  }

  function registerRequestInterceptor(interceptor) {
    if (typeof interceptor !== 'function') {
      warn('Knife4j request interceptor must be a function.');
      return;
    }
    requestInterceptors.push(interceptor);
  }

  function applyRequestInterceptors(config, context) {
    var nextConfig = config || {};
    var nextContext = context || {};
    nextConfig.headers = nextConfig.headers || {};
    for (var i = 0; i < requestInterceptors.length; i++) {
      try {
        var returnedConfig = requestInterceptors[i](nextConfig, nextContext);
        if (returnedConfig && typeof returnedConfig.then === 'function') {
          warn('Knife4j request interceptor must return config synchronously.');
        } else if (returnedConfig) {
          nextConfig = returnedConfig;
          nextConfig.headers = nextConfig.headers || {};
        }
      } catch (error) {
        warn('Knife4j request interceptor failed.', error);
      }
    }
    return nextConfig;
  }

  function loadScript(scriptUrl) {
    if (!env.document || !env.document.createElement) {
      return Promise.resolve();
    }
    if (loadedScriptUrls[scriptUrl]) {
      return Promise.resolve();
    }
    loadedScriptUrls[scriptUrl] = true;
    return new Promise(function (resolve) {
      var script = env.document.createElement('script');
      script.src = scriptUrl;
      script.async = false;
      script.onload = function () {
        resolve();
      };
      script.onerror = function (error) {
        warn('Knife4j custom script load failed: ' + scriptUrl, error);
        resolve();
      };
      (env.document.head || env.document.body).appendChild(script);
    });
  }

  function loadCustomScripts(scriptUrls) {
    var urls = normalizeScriptUrls(scriptUrls);
    var chain = Promise.resolve();
    urls.forEach(function (scriptUrl) {
      chain = chain.then(function () {
        return loadScript(scriptUrl);
      });
    });
    return chain;
  }

  var api = {
    registerRequestInterceptor: registerRequestInterceptor,
    applyRequestInterceptors: applyRequestInterceptors,
    loadCustomScripts: loadCustomScripts,
    _requestInterceptors: requestInterceptors
  };

  if (env.window) {
    var existingApi = env.window.Knife4j || {};
    requestInterceptors = existingApi._requestInterceptors || requestInterceptors;
    api._requestInterceptors = requestInterceptors;
    api.registerRequestInterceptor = registerRequestInterceptor;
    api.applyRequestInterceptors = applyRequestInterceptors;
    api.loadCustomScripts = loadCustomScripts;
    env.window.Knife4j = Object.assign(existingApi, api);
    return env.window.Knife4j;
  }

  return api;
}

module.exports = {
  createKnife4jExtension: createKnife4jExtension,
  normalizeScriptUrls: normalizeScriptUrls
};
