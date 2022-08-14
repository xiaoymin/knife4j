@Tag(name = "首页模块")
@RestController
public class IndexController {

    @Parameter(name = "name",description = "姓名",required = true)
    @Operation(summary = "向客人问好")
    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi(@RequestParam(value = "name")String name){
        return ResponseEntity.ok("Hi:"+name);
    }
}