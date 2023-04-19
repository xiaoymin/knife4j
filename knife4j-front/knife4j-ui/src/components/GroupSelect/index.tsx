/**
 * 文档分组选择器
 */

import { Select } from 'antd';
import { useState } from 'react';
import { useModel } from 'umi';

const GroupSelect = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  const [groupValue, setGroupValue] = useState('default');

  const onChange = (value: string) => {
    setGroupValue(value);
    setInitialState({
      ...initialState,
      groupId: value,
    } as any);
  };

  return (
    <>
      <Select
        showSearch
        style={{ width: '100%' }}
        placeholder="Select a person"
        optionFilterProp="children"
        onChange={onChange}
        // onSearch={onSearch}
        filterOption={(input, option) => (option?.label ?? '').includes(input)}
        value={groupValue}
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
