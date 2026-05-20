const assert = require('assert');
const path = require('path');

const extension = require('../knife4j-vue/src/core/Knife4jExtension.cjs');

async function testRequestInterceptors() {
  const warnMessages = [];
  const api = extension.createKnife4jExtension({
    console: {
      warn: function (message) {
        warnMessages.push(String(message));
      }
    }
  });

  api.registerRequestInterceptor(function (config, context) {
    assert.strictEqual(context.requestType, 'raw');
    config.headers['x-request-id'] = 'rid-1';
    return config;
  });
  api.registerRequestInterceptor(function () {
    throw new Error('broken interceptor');
  });
  api.registerRequestInterceptor(function (config) {
    config.headers['x-extra'] = 'ok';
  });

  const requestConfig = api.applyRequestInterceptors({
    url: '/pets',
    headers: {}
  }, {
    requestType: 'raw'
  });

  assert.strictEqual(requestConfig.headers['x-request-id'], 'rid-1');
  assert.strictEqual(requestConfig.headers['x-extra'], 'ok');
  assert.strictEqual(warnMessages.length, 1);
}

async function testPromiseInterceptorIsIgnored() {
  const warnMessages = [];
  const api = extension.createKnife4jExtension({
    console: {
      warn: function (message) {
        warnMessages.push(String(message));
      }
    }
  });

  api.registerRequestInterceptor(function (config) {
    assert.strictEqual(config.headers['x-sync'], 'ok');
    return Promise.resolve({
      headers: {
        'x-async': 'ignored'
      }
    });
  });

  const requestConfig = api.applyRequestInterceptors({
    url: '/pets',
    headers: {
      'x-sync': 'ok'
    }
  }, {
    requestType: 'form'
  });

  assert.strictEqual(requestConfig.headers['x-sync'], 'ok');
  assert.strictEqual(requestConfig.headers['x-async'], undefined);
  assert.strictEqual(typeof requestConfig.then, 'undefined');
  assert.strictEqual(warnMessages.length, 1);
}

async function testScriptLoading() {
  const appended = [];
  const document = {
    createElement: function (tagName) {
      assert.strictEqual(tagName, 'script');
      return {};
    },
    head: {
      appendChild: function (script) {
        appended.push(script.src);
        setImmediate(script.onload);
      }
    }
  };
  const api = extension.createKnife4jExtension({ document });

  await api.loadCustomScripts([
    '',
    '/knife4j/custom/a.js',
    '/knife4j/custom/b.js',
    '/knife4j/custom/a.js'
  ]);

  assert.deepStrictEqual(appended, [
    '/knife4j/custom/a.js',
    '/knife4j/custom/b.js'
  ]);
}

async function main() {
  assert.strictEqual(path.basename(__filename), 'custom-script-extension.test.cjs');
  await testRequestInterceptors();
  await testPromiseInterceptorIsIgnored();
  await testScriptLoading();
}

main().catch(error => {
  console.error(error);
  process.exit(1);
});
