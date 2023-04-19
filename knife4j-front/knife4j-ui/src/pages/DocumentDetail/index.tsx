import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import React from 'react';
import { useParams } from 'umi';

const Welcome: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const params = useParams();
  console.log('详情页面：', params);

  return (
    <PageContainer>
      <div>hahahh来试试菜单</div>
    </PageContainer>
  );
};

export default Welcome;
