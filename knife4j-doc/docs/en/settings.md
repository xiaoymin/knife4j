# Settings

The personalization function is the personalized setting function provided by SwaggerBootstrapUi for its own Ui features, including:

- Turn on request parameter caching
- Menu Api address display
- group tag display description description attribute
- Enable RequestMapping interface type duplicate address filtering
- Turn on SwaggerBootstrapUi enhancements.

Function Directory: Document Management -> Personalization

![](/knife4j/images/settings.png)

**Open request parameter cache**

This function can be seen in the online debugging. When you click Send Debug View for each interface, when you open the interface and debug later, the default is to retain the last sent interface parameter information.

If you don't want to enable this cache, leave this item unchecked. The default is true, that is, it is on.

**Menu Api address display**

The menu Api address is displayed in the left menu does not display api address information, the default is false, that is, not displayed, the default effect is as follows
![](/knife4j/images/url-no.png)

If you need to display the interface address on the left menu bar, check this box to display the following effect:

![](/knife4j/images/url-display.png)

**Group tag display description description attribute**

Whether the tag displays the description attribute in the code, the default is false, and does not display. If the description attribute is checked, the effect diagram is as follows:

![](/knife4j/images/tag-desc.png)

**Enable RequestMapping interface type duplicate address filtering**

For the interface of the backend RequestMapping annotation type, if the developer does not specify the interface type, the default use of Swagger will generate seven different types of interface addresses, the effect diagram is as follows:

![](/knife4j/images/rp-multipar.png)

In some cases, developers may need to filter to simplify duplicate interface documentation. At this point, the developer selects this option and selects the option to display the interface type. SwaggerBootstrapUi will automatically filter based on this option.

For example, check and then display the Post type by default, the effect is as follows:![](/knife4j/images/rp-multipar-filter.png)

This item defaults to false, which means that this item is not turned on (no filtering).

**Turn on SwaggerBootstrapUi enhancements**

After enabling this item, you can use the enhancements of SwaggerBootstrapUi. For enhancements, refer to the [Enhancements section](enh-func.md) for instructions.
 
 <icp/> 
 comment/> 
 
 
 
 