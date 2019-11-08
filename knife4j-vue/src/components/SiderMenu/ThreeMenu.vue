
<script>
export default {
  name: "ThreeMenu",
  functional: true,
  props: {
    menuData: {
      type: Array,
      default: () => {
        [];
      }
    }
  },
  render(h, context) {
    const { menuData } = context.props;
    const vnodes = [];

    const getMenuItemPath = item => {
      return (
        <router-link to={item.path}>
          {item.icon ? <a-icon type={item.icon} /> : ""}
          <span>{item.name}</span>
        </router-link>
      );
    };

    const getSubMenuOrItem = item => {
      if (item.children && item.children.some(child => child.name)) {
        const childrenItems = getNavMenuItems(item.children); // eslint-disable-line
        // 当无子菜单时就不展示菜单
        if (childrenItems && childrenItems.length > 0) {
          return (
            <a-sub-menu
              key={item.path}
              title={
                item.icon ? (
                  <span>
                    <a-icon type={item.icon} />
                    <span>{item.name}</span>
                  </span>
                ) : (
                  <span>{item.name}</span>
                )
              }
            >
              {childrenItems}
            </a-sub-menu>
          );
        }
        return null;
      } else {
        return (
          <a-menu-item key={item.path}>{getMenuItemPath(item)}</a-menu-item>
        );
      }
    };

    const getNavMenuItems = data => {
      if (!data) {
        return [];
      }
      return data.map(item => {
        return getSubMenuOrItem(item);
      });
    };

    const DOM = getNavMenuItems(menuData);

    vnodes.push(DOM);
    return vnodes;
  }
};
</script>
