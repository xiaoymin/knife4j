/***
 * knife4j常量
 */
const constants = {
  //全局离线参数
  globalParameter: "Knife4jOfficeParameter",
  //全局离线参数表头
  globalParameterTableColumns: [{
      title: "参数名称",
      dataIndex: "name",
      width: "20%",
      scopedSlots: {
        customRender: "name"
      }
    },
    {
      title: "参数值",
      className: "column-money",
      dataIndex: "value",
      width: "40%"
    },
    {
      title: "参数类型",
      dataIndex: "in",
      width: "20%"
    },
    {
      title: "操作",
      dataIndex: "operation",
      scopedSlots: {
        customRender: "operation"
      }
    }
  ]
}

export default constants
