/**
 * 文档分组选择器
 */

import { Select } from 'antd';

const GroupSelect = () => {
  return (
    <>
      <Select
        showSearch
        style={{ width: '100%' }}
        placeholder="Select a person"
        optionFilterProp="children"
        // onChange={onChange}
        // onSearch={onSearch}
        filterOption={(input, option) => (option?.label ?? '').includes(input)}
        value={'default'}
        options={[
          {
            value: 'default',
            label: '默认分组',
          },
          {
            value: '2',
            label: '用户管理',
          },
          {
            value: '3',
            label: '系统管理',
          },
        ]}
      />
    </>
  );
};

export default GroupSelect;
