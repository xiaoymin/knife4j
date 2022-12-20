import KUtils from '../utils'

/**
 * OAuth2认证的支持
 * @param {*} grantType  oauth2的授权类型
 * @param {*} tokenUrl  请求touken地址
 * @param {*} authUrl 授权地址
 */
function SwaggerBootstrapUiOAuth2(grantType, tokenUrl, authUrl, instanceId) {
    this.grantType = grantType;
    this.name = 'Authorization';
    this.username = null;
    this.password = null;
    this.redirectUri = null;
    // 是否已经授权
    this.granted = false;
    this.tokenUrl = tokenUrl;
    this.authorizeUrl = authUrl;
    this.clientId = '';
    this.clientSecret = '';
    // 授权后返回值
    this.accessToken = null;
    this.tokenType = null;
    this.state = 'OAuth' + instanceId;
}
/**
 * 授权过后从本地LocalStorage同步
 */
SwaggerBootstrapUiOAuth2.prototype.syncOAuth = function () {
    var that = this;
    if (window.localStorage) {
        var key = that.state;
        var value = window.localStorage.getItem(key);
        if (KUtils.strNotBlank(value)) {
            var storageObject = KUtils.json5parse(value);
            this.accessToken = KUtils.getValue(storageObject, 'tokenType', 'Bearer', true) + ' ' + storageObject.accessToken;
            this.tokenType = storageObject.tokenType;
            this.granted = true;
        }
    }
}
/**
 * 保存自己
 */
SwaggerBootstrapUiOAuth2.prototype.sync = function () {
    // console.log('saveOAuthMySELF')
    this.syncOAuth();
    if (window.localStorage) {
        var key = 'SELF' + this.state;
        var cacheValue = window.localStorage.getItem(key);
        if (KUtils.strNotBlank(cacheValue)) {
            // 缓存中存在
            var cacheObject = KUtils.json5parse(cacheValue);
            // 判断授权形式是否相同
            if (this.grantType == cacheObject.grantType) {
                // 相等
                // 是否已经授权
                this.granted = cacheObject.granted;
                if (KUtils.strBlank(this.clientId)) {
                    this.clientId = cacheObject.clientId;
                }
                if (KUtils.strBlank(this.clientSecret)) {
                    this.clientSecret = cacheObject.clientSecret;
                }
                if (KUtils.strBlank(this.redirectUri)) {
                    this.redirectUri = cacheObject.redirectUri;
                }
                if (KUtils.strBlank(this.username)) {
                    this.username = cacheObject.username;
                }
                if (KUtils.strBlank(this.password)) {
                    this.password = cacheObject.password;
                }
                // 授权后返回值
                if (KUtils.strBlank(this.accessToken)) {
                    this.accessToken = cacheObject.accessToken;
                }
                if (KUtils.strBlank(this.tokenType)) {
                    this.tokenType = cacheObject.tokenType;
                }
            }
            window.localStorage.setItem(key, KUtils.json5stringify(this));
        } else {
            window.localStorage.setItem(key, KUtils.json5stringify(this));
        }
    }

}
/**
 * 注销退出
 */
SwaggerBootstrapUiOAuth2.prototype.clear = function () {
    this.username = null;
    this.password = null;
    this.accessToken = null;
    this.redirectUri = null;
    this.granted = false;
    this.clientId = '';
    this.clientSecret = '';
    // 授权后返回值
    this.accessToken = null;
    this.tokenType = null;
    var key = 'SELF' + this.state;
    window.localStorage.setItem(key, KUtils.json5stringify(this));
}


export default SwaggerBootstrapUiOAuth2;