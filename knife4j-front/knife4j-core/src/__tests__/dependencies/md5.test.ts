import md5 from 'crypto-js/md5'
import Base64 from 'crypto-js/enc-base64';

test("testMd5", () => {
    console.log("md5tester.key:" + md5('12我爱TypeScript'))
    console.log("base64:" + Base64.parse("12我爱TypeScript"))
    console.log("base641:" + Base64.parse("abcdefg"));
})