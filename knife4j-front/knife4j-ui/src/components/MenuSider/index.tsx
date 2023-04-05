import type { HeaderViewProps } from '@ant-design/pro-layout/es/components/Header';
import { throttle } from 'lodash';
import {
  cloneElement,
  CSSProperties,
  Dispatch,
  MutableRefObject,
  SetStateAction,
  useEffect,
  useRef,
  useState,
} from 'react';

/**
 * 实现元素左右拖拽的Hook逻辑
 * @param resizeLine div
 * @param setNavWidth 宽度
 */
function useLeft2Right(
  resizeLine: MutableRefObject<HTMLDivElement | null>,
  setNavConfig: Dispatch<
    SetStateAction<{
      buttonDown: boolean;
      siderWidth: number;
    }>
  >,
) {
  let siderWidth = 0;

  /**
   * 鼠标按下的方法
   */
  let mouseDown = (resizeLine: HTMLElement) => {
    //拖动的时候禁止页面选中
    document.body.onselectstart = function () {
      return false;
    };
    resizeLine.style.borderRight = '1px dashed #222222';
    let resize = throttle(function (e: MouseEvent) {
      if (e.clientX > 150 && e.clientX < window.innerWidth * 0.8) {
        siderWidth = e.clientX;
        setNavConfig({ siderWidth, buttonDown: true });
      }
    }, 50);

    /**
     * 鼠标弹起的方法
     */
    let resizeUp = function () {
      document.body.onselectstart = function () {
        return true;
      };
      resizeLine.style.borderRight = 'none';
      document.removeEventListener('mousemove', resize);
      document.removeEventListener('mouseup', resizeUp);
      setNavConfig({ siderWidth, buttonDown: false });
    };

    document.addEventListener('mousemove', resize);
    document.addEventListener('mouseup', resizeUp);
  };

  useEffect(() => {
    let { current } = resizeLine;
    if (current) {
      const line = current as HTMLElement;
      line.addEventListener('mousedown', () => mouseDown(line));
      return function () {
        line.removeEventListener('mousedown', () => mouseDown(line));
      };
    }
  }, []);
}

/**
 * 可以拖拽改变宽度的侧边栏组件
 * @param props 属性
 */
function DragSider(props: { children: React.ReactNode; siderWidth: number }) {
  let { children, siderWidth } = props;

  let [navConfig, setNavConfig] = useState({
    buttonDown: false,
    siderWidth,
  });

  let [navWidth, setNavWidth] = useState(siderWidth);

  let resizeLine = useRef<HTMLDivElement>(null);

  useLeft2Right(resizeLine, setNavConfig);

  let asideStyle: CSSProperties = {
    width: navWidth,
    position: 'relative',
  };

  let resizeLineStyle: CSSProperties = {
    position: 'absolute',
    right: 0,
    top: 0,
    left: navConfig.siderWidth,
    zIndex: 1000,
    width: 6,
    height: '100vh',
    cursor: 'w-resize',
  };

  useEffect(() => {
    if (!navConfig.buttonDown) {
      setNavWidth(navConfig.siderWidth);
    }
  }, [navConfig]);

  return (
    <aside style={asideStyle}>
      <div ref={resizeLine} style={resizeLineStyle} />
      {cloneElement(children as React.ReactElement, { siderWidth: navWidth })}
    </aside>
  );
}

export type MenuSiderProps = {
  menu: HeaderViewProps;
  defaultDom: React.ReactNode;
};

const MenuSider = (props: MenuSiderProps) => {
  return (
    <>
      <DragSider siderWidth={props.menu.siderWidth as number}>{props.defaultDom}</DragSider>
    </>
  );
};

export default MenuSider;
