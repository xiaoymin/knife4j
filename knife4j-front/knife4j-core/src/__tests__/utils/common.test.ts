import CommonUtils from '../../utils/common';

test('testNumber', () => {
  const n = CommonUtils.getNumber();
  expect(1).toBe(n);
  console.log('getNumber:', n);
});
