import KUtils from '../utils'

import SwaggerBootstrapUiOAuth2 from '../oas/SwaggerBootstrapUiOAuth2'


/**
 * OAuth2解析规则
 * @param {*} originalType 原始类型
 * @param {*} instanceId 实例id
 * @param {*} oauth2Definitions 原始definitions
 */
function Knife4jOAS3OAuth2Reader(originalType, instanceId, oauth2Definitions) {
    this.originalType = originalType;
    this.instanceId = instanceId;
    this.oauth2Definitions = oauth2Definitions;
}

/**
 * 解析OAuth2
 */
Knife4jOAS3OAuth2Reader.prototype.readOAuth2 = function () {
    let grantType = this.originalType;
    let tokenUrl = '';
    let authUrl = '';
    if (KUtils.checkUndefined(this.oauth2Definitions)) {
        authUrl = KUtils.propValue('authorizationUrl', this.oauth2Definitions, '');
        tokenUrl = KUtils.propValue('tokenUrl', this.oauth2Definitions, '');
    }
    //兼容OAS3的OAuth2类型，在Knife4j的Ui进行适配
    if (this.originalType === 'authorizationCode') {
        grantType = 'accessCode';
    } else if (this.originalType === 'clientCredentials') {
        grantType = 'client_credentials';
    }
    return new SwaggerBootstrapUiOAuth2(grantType, tokenUrl, authUrl, this.instanceId);
}

export default Knife4jOAS3OAuth2Reader;