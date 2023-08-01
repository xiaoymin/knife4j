import { useState } from 'react';
import { Menu } from 'antd';
import './ResizableMenu.css'; // Create this CSS file for custom styles

const ResizableMenu = () => {
    const [sidebarWidth, setSidebarWidth] = useState(250);

    const handleDrag = (e) => {
        if (e.clientX > 100 && e.clientX < 400) {
            // Limit the sidebar width between 100px and 400px
            setSidebarWidth(e.clientX);
        }
    };

    const handleDragEnd = () => {
        window.removeEventListener('mousemove', handleDrag);
        window.removeEventListener('mouseup', handleDragEnd);
    };

    const handleMouseDown = () => {
        window.addEventListener('mousemove', handleDrag);
        window.addEventListener('mouseup', handleDragEnd);
    };

    return (
        <div className="resizable-menu">
            <div className="sidebar" style={{ flex: `0 0 ${sidebarWidth}px` }}>
                <Menu
                    defaultSelectedKeys={['1']}
                    defaultOpenKeys={['sub1']}
                    mode="inline"
                    theme="dark"
                >
                    <Menu.SubMenu key="sub1" title="Menu">
                        <Menu.Item key="1">Item 1</Menu.Item>
                        <Menu.Item key="2">Item 2</Menu.Item>
                        {/* Add more menu items as needed */}
                    </Menu.SubMenu>
                </Menu>
                <div
                    className="drag-handle"
                    onMouseDown={handleMouseDown}
                ></div>
            </div>
            <div className="content">
                {/* Your content goes here */}
            </div>
        </div>
    );
};

export default ResizableMenu;
