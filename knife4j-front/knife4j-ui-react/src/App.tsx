import React, { useState, useRef } from 'react';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  SearchOutlined,
  SettingOutlined,
  UploadOutlined,
  UserOutlined,
  VideoCameraOutlined,
} from '@ant-design/icons';
import { Layout, Menu, Button, Select, Input, ConfigProvider, Tabs, theme } from 'antd';
import { Resizable } from 'react-resizable';

const { Header, Sider, Content, Footer } = Layout;
type TargetKey = React.MouseEvent | React.KeyboardEvent | string;
const defaultPanes = new Array(2).fill(null).map((_, index) => {
  const id = String(index + 1);
  return { label: `Tab ${id}`, children: `Content of Tab Pane ${index + 1}`, key: id };
});

const footerStyle: React.CSSProperties = {
  textAlign: 'center',
  fontSize: '14px',
  color: '#848587',
  backgroundColor: '#f0f2f5',
  minHeight: '40px'
};


const App: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [siderWidth, setSiderWidth] = useState(320);

  const handleResize = (_e: React.SyntheticEvent, data: { size: { width: number; height: number } }) => {
    setSiderWidth(data.size.width);
  };
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  // tabs

  const [activeKey, setActiveKey] = useState(defaultPanes[0].key);
  const [items, setItems] = useState(defaultPanes);
  const newTabIndex = useRef(0);

  const onChange = (key: string) => {
    setActiveKey(key);
  };

  const add = () => {
    const newActiveKey = `newTab${newTabIndex.current++}`;
    setItems([...items, { label: 'New Tab', children: 'New Tab Pane', key: newActiveKey }]);
    setActiveKey(newActiveKey);
  };

  const remove = (targetKey: TargetKey) => {
    const targetIndex = items.findIndex((pane) => pane.key === targetKey);
    const newPanes = items.filter((pane) => pane.key !== targetKey);
    if (newPanes.length && targetKey === activeKey) {
      const { key } = newPanes[targetIndex === newPanes.length ? targetIndex - 1 : targetIndex];
      setActiveKey(key);
    }
    setItems(newPanes);
  };

  const onEdit = (targetKey: TargetKey, action: 'add' | 'remove') => {
    if (action === 'add') {
      add();
    } else {
      remove(targetKey);
    }
  };

  return (
    <ConfigProvider theme={{
      components: {
        Menu: {

        }
      },
      token: {

      },
    }}>
      <Layout>
        <Resizable
          width={siderWidth}
          height={Infinity}
          handle={<div className="react-resizable-handle" />}
          resizeHandles={['e']}
          onResize={handleResize}
          minConstraints={[300, Infinity]}
          maxConstraints={[520, Infinity]}
          draggableOpts={{ enableUserSelectHack: false }}
        >
          <Sider trigger={true} collapsible collapsed={collapsed} width={siderWidth}>
            <div style={{ display: !collapsed ? 'none' : 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: '5px', }}>
              <img src='/src/assets/logo/k.png' style={{ width: '32px', height: '32px' }} />
            </div>
            <div style={{ display: collapsed ? 'none' : 'flex', justifyContent: 'center', alignItems: 'center', marginBottom: '5px', marginTop: '5px' }}>
              <Select
                labelInValue
                defaultValue={{ value: 'lucy', label: 'Lucy (101)' }}
                style={{ width: siderWidth - 10 }}
                options={[
                  {
                    value: 'jack',
                    label: 'Jack (100)',
                  },
                  {
                    value: 'lucy',
                    label: 'Lucy (101)',
                  },
                ]}
              />
            </div>

            <Menu
              theme="dark"
              mode="inline"
              defaultSelectedKeys={['1']}
              items={[
                {
                  key: '1',
                  icon: <UserOutlined />,
                  label: '主页',
                },
                {
                  key: '2',
                  icon: <VideoCameraOutlined />,
                  label: '实体类列表',
                },
                {
                  key: '3',
                  icon: <UploadOutlined />,
                  label: '文档管理',
                }, {
                  key: '4',
                  icon: <UploadOutlined />,
                  label: '用户管理模块',
                }, {
                  key: '5',
                  icon: <UploadOutlined />,
                  label: '鉴权中心模块',
                }, {
                  key: '6',
                  icon: <UploadOutlined />,
                  label: '开放接口模块',
                },
              ]}
            />
          </Sider>
        </Resizable>
        <Layout>
          <Header style={{ padding: 0, background: colorBgContainer, display: 'flex', alignItems: 'center' }}>
            <Button
              type="text"
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              style={{
                fontSize: '16px',
                width: 64,
                height: 64,
              }}
            />
            OpenAPI接口文档聚合中心
            <span style={{ marginLeft: 'auto' }}>
              {/* Search box */}
              <Input
                placeholder="Search..."
                prefix={<SearchOutlined />}
                style={{ marginRight: '10px', width: '200px' }}
              />

              {/* Settings icon */}
              <Button
                type="text"
                icon={<SettingOutlined />}
                style={{
                  fontSize: '16px',
                  width: 48,
                  height: 48,
                }}
              />
            </span>
          </Header>
          <Content style={{ margin: '6px 4px', minHeight: 610, padding: 6, background: colorBgContainer }}>
            {/* 设置Content容器自适应高度 */}
            <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
              <Tabs
                hideAdd
                onChange={onChange}
                activeKey={activeKey}
                type="editable-card"
                onEdit={onEdit}
                items={items}
                // 将Tabs的父容器高度设置为100%
                style={{ flex: 1, margin: '2px 2px' }}
              />
            </div>
          </Content>
          <Footer style={footerStyle}>Apache License 2.0 | Copyright  2019-Knife4j v5.0</Footer>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
};

export default App;
