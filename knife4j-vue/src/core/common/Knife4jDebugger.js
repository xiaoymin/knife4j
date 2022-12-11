import KUtils from '../utils'

/**
 * Knife4j-Debugger调试工具
 */
const Knife4jDebugger = {
    /**
     * OAS3规范解析Blob类型数据
     * @param {*} res 响应内容
     * @param {*} callback 回调函数
     */
    resolveBlobResponse(res, callback) {
        if (KUtils.checkUndefined(res)) {
            var resp = res.request;
            var headers = res.headers;
            if (KUtils.checkUndefined(resp)) {
                //console.log('resp,', resp)
                // 判断是否是blob类型
                var contentDisposition = KUtils.propValue("content-disposition", headers, "");
                if (resp.responseType == "blob" || KUtils.strNotBlank(contentDisposition)) {
                    // 针对OpenAPI3的规范,由于统一添加了blob类型，此处需要判断
                    let responseTextType = res.data.type;
                    if (responseTextType == "application/json" ||
                        responseTextType == "application/xml" ||
                        responseTextType == "text/html" ||
                        responseTextType == "text/plain") {
                        // 服务端返回JSON数据,Blob对象转为JSON
                        const reader = new FileReader();
                        reader.onload = e => {
                            let readerResponse = {
                                responseText: e.target.result,
                                response: e.target.result,
                                responseTextType: responseTextType,
                                status: resp.status,
                                headers: res.headers,
                                statusText: resp.statusText,
                                readyState: resp.readyState,
                                timeout: resp.timeout,
                                withCredentials: resp.withCredentials
                            }
                            callback(readerResponse);
                        };
                        reader.readAsText(res.data);
                    }
                } else {
                    let readerResponse = {
                        responseText: res.data,
                        response: res.data,
                        responseTextType: '',
                        status: resp.status,
                        headers: res.headers,
                        statusText: resp.statusText,
                        readyState: resp.readyState,
                        timeout: resp.timeout,
                        withCredentials: resp.withCredentials
                    }
                    callback(readerResponse);
                }
            } else {
                let readerResponse = {
                    responseText: res.data,
                    response: res.data,
                    responseTextType: '',
                    status: resp.status,
                    headers: res.headers,
                    statusText: resp.statusText,
                    readyState: resp.readyState,
                    timeout: resp.timeout,
                    withCredentials: resp.withCredentials
                }
                callback(readerResponse);
            }
        }
    }
}

export default Knife4jDebugger;