/**
 * 解析OpenAPI3规范
 */
function abc() {
  const a = 1;
  return post(a);
}

const post = async (a: any) => {
  if (a === 1) {
    return await Promise.resolve(22);
  }
  return await Promise.resolve(11);
};
