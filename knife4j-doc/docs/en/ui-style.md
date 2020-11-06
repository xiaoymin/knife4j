# Ui-Style

Friends who have used swagger-bootstrap-ui should know that it is based on the layout of the left and right menus. This is similar to most of the current background management systems. The reason for using this style is that it should be more in line with the operation of the Chinese. Get used to it.

Compared with the structure of swagger-ui, which is laid out in turn, I think this method is more suitable for interface docking personnel.

![](/knife4j/images/des.png)

Each interface document is mainly introduced through the document description and online debugging two core tab components. At the same time, opening the document description will expand the document description in multiple tabs, and switch back and forth to view the different interface document descriptions, which is convenient and quick.

as follows：

![](/knife4j/images/mul-tab.png)

**Api Description**：According to Swagger's standard JSON file, the information description of the interface is listed in detail, including: interface address, interface type, producers, consumes, interface parameter field description, request example, response parameter description, response status code, response example.

![](/knife4j/images/desc.png)

**Online Debug**：The developer can debug the interface based on this document. ui will automatically list the request parameters, request address and other information according to the interface information. The developer only needs to fill in the corresponding field value to coordinate the test.

![](/knife4j/images/debug-online.png)
 
 
 