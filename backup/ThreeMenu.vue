
<script>
export default {
  name: "ThreeMenu",
  functional: true,
  props: {
    enableVersion:{
      type: Boolean,
      default: false
    },
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
    const vnodes = [];
    const getMenuItemPath = item => {
      //console.log(item);
      if (item.deprecated) {
        var depClass="knife4j-menu-api-deprecated "+item.menuClass;
        if (collapsed) {
          //收缩后,不显示版本控制的标识
          return (
            <router-link class={depClass} to={item.path}>
              {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
              <span>{item.name}</span>
            </router-link>
          );
        } else {
          if (item.hasNew) {
            return (
              <router-link class={depClass} to={item.path}>
                {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
                <a-badge
                  status="processing"
                  title="新接口"
                  style="margin-bottom:3px;"
                />
                {item.method?<span class="knife4j-menu-line">{item.method}</span>:""}
                <span>{item.name}</span>
              </router-link>
            );
          } else {
            return (
              <router-link class={depClass} to={item.path}>
                {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
                {item.method?<span class="knife4j-menu-line">{item.method}</span>:""}
                <span>{item.name}</span>
              </router-link>
            );
          }
        }
      } else {
        if (collapsed) {
          //收缩后,不显示版本控制的标识
          return (
            <router-link to={item.path}>
              {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
              <span>{item.name}</span>
            </router-link>
          );
        } else {
          if (item.hasNew) {
            return (
              <router-link class={item.menuClass} to={item.path}>
                {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
                <a-badge
                  status="processing"
                  title="新接口"
                  style="margin-bottom:3px;"
                />
                {item.method?<span class="knife4j-menu-line">{item.method}</span>:""}
                <span>{item.name}</span>
              </router-link>
            );
          } else {
            return (
              <router-link class={item.menuClass} title={item.name} to={item.path}>
                {item.icon ? <my-icon type={item.icon}></my-icon> : ""}
                {item.method?<span class="knife4j-menu-line">{item.method}</span>:""}
                <span>{item.name}</span>
              </router-link>
            );
          }
        }
      }
    };

    const getSubMenuOrItem = item => {
      if (item.children && item.children.some(child => child.name)) {
        const childrenItems = getNavMenuItems(item.children); // eslint-disable-line
        // 当无子菜单时就不展示菜单
        if (childrenItems && childrenItems.length > 0) {
          if (collapsed) {
            //收缩后,不显示版本控制的标识
            return (
              <a-sub-menu
                key={item.key}
                title={
                  item.icon ? (
                    <span>
                      <my-icon type={item.icon}></my-icon>
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
          } else {
            if (item.hasNew) {
              return (
                <a-sub-menu
                  key={item.key}
                  title={
                    item.icon ? (
                      <span>
                        <my-icon type={item.icon}></my-icon>
                        <a-badge
                          status="processing"
                          title="新接口"
                          style="margin-bottom:3px;"
                        />
                        <span>{item.name}</span>
                        {item.num?<span class='knife4j-menu-badge-num'>{item.num}</span>:""}
                      </span>
                    ) : (
                      <span>
                      <span>{item.name}</span>
                       {item.num?<span class='knife4j-menu-badge-num'>{item.num}</span>:""}
                       </span>
                    )
                  }
                >
                  {childrenItems}
                </a-sub-menu>
              );
            } else {
              return (
                <a-sub-menu
                  key={item.key}
                  title={
                    item.icon ? (
                      <span>
                        <my-icon type={item.icon}></my-icon>
                        <span>{item.name}</span>
                        {item.num?<span class='knife4j-menu-badge-num'>{item.num}</span>:""}
                      </span>
                    ) : (
                      <span>
                      <span>{item.name}</span>
                       {item.num?<span class='knife4j-menu-badge-num'>{item.num}</span>:""}
                       </span>
                    )
                  }
                >
                  {childrenItems}
                </a-sub-menu>
              );
            }
          }
        }
        return null;
      } else {
        return (
          <a-menu-item key={item.key}>{getMenuItemPath(item)}</a-menu-item>
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
