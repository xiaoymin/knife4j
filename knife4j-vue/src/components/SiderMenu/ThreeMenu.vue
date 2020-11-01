<script>
import ThreeRoute from "./ThreeRoute";
import ThreeTitle from "./ThreeTitle";

export default {
  name: "ThreeMenu",
  functional: true,
  props: {
    menuData: {
      type: Array,
      default: () => {
        [];
      }
    },
    collapsed: {
      type: Boolean,
      default: false
    }
  },
  render(h, context) {
    const { menuData } = context.props;
    const collapsed = context.props.collapsed;

    const getSubMenuOrItem = (item) => {
      if (item.children && item.children.some((child) => child.name)) {
        const childrenItems = getNavMenuItems(item.children); // eslint-disable-line

        if (childrenItems && childrenItems.length > 0) {
          return (
            <a-sub-menu
              key={item.key}
              title={<ThreeTitle collapsed={collapsed} item={item} />}
            >
              {childrenItems}
            </a-sub-menu>
          );
        }
        // 当无子菜单时就不展示菜单
        return null;
      } else {
        return (
          <a-menu-item key={item.key}>
            <ThreeRoute item={item} />
          </a-menu-item>
        );
      }
    };

    const getNavMenuItems = (data) => {
      if (!data) {
        return [];
      }
      return data.map((item) => {
        return getSubMenuOrItem(item);
      });
    };
    return getNavMenuItems(menuData);
  },
};
</script>