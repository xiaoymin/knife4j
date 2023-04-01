import CommonUtils from '../../utils/common'



test("testNumber", () => {
    let n = CommonUtils.getNumber();
    expect(1).toBe(n)
    console.log("getNumber:", n)
})
