# springfox 源码分析(十九) guava库学习


我们在研究springfox的过程中，发现springfox大量使用了guava这个库的一些方法和类,针对我们在研究源码的学习过程中,将涉及到的guava库中的类进行归纳总结,后期在工作中我们也可以熟练运用guava库为我们提供的简介api

## ArrayListMultimap

从字面意思来看map中包含了数组元素

举一个简单的例子来帮助我们理解这个类

我们在读书时,作为主体对象的我们(学生),都有一个班级,即该学生是属于哪个班级的,现在拥有全校的学生列表集合,但是想通过班级名称来统计各个班级的人数情况,应该如何操作呢？

如果在数据库中,一个SQL语句的分组函数即可搞定,但是在Java中却是异常繁杂.

ArrayListMultimap就可以做到

先来看我们的学生类：

```java
public class Student {

    private String name;

    private Integer age;
    /***
     * 班级
     */
    private String classRoom;
    //getter setter
}
```

学生类拥有姓名、年龄、班级三个属性，此时我们通过ArrayListMultimap来解决上面我们所提的问题：

```java
List<Student> list= Lists.newArrayList();
list.add(new Student("学生A",12,"班级1"));
list.add(new Student("学生B",13,"班级2"));
list.add(new Student("学生C",12,"班级1"));
list.add(new Student("学生D",15,"班级3"));
list.add(new Student("学生E",12,"班级1"));
list.add(new Student("学生F",13,"班级2"));
list.add(new Student("学生G",11,"班级1"));
list.add(new Student("学生H",15,"班级2"));
list.add(new Student("学生I",11,"班级3"));
list.add(new Student("学生J",12,"班级1"));
list.add(new Student("学生W",16,"班级1"));
list.add(new Student("学生Q",13,"班级4"));

//针对班级分组
ArrayListMultimap<String,Student> arrayListMultimap=ArrayListMultimap.create();
for (Student student:list){
    arrayListMultimap.put(student.getClassRoom(),student);
}
Map<String,Collection<Student>> map=arrayListMultimap.asMap();
for (String key:map.keySet()){
    Collection<Student> students=map.get(key);
    System.out.println("班级名称:"+key+",总共有学员:"+students.size());
    for (Student sd:students){
        System.out.println(sd.toString());
    }
    System.out.println("");
}
```

而此时,控制台的打印情况如下：

```properties
班级名称:班级1,总共有学员:6
班级：班级1,姓名：学生A,年龄:12
班级：班级1,姓名：学生C,年龄:12
班级：班级1,姓名：学生E,年龄:12
班级：班级1,姓名：学生G,年龄:11
班级：班级1,姓名：学生J,年龄:12
班级：班级1,姓名：学生W,年龄:16

班级名称:班级4,总共有学员:1
班级：班级4,姓名：学生Q,年龄:13

班级名称:班级2,总共有学员:3
班级：班级2,姓名：学生B,年龄:13
班级：班级2,姓名：学生F,年龄:13
班级：班级2,姓名：学生H,年龄:15

班级名称:班级3,总共有学员:2
班级：班级3,姓名：学生D,年龄:15
班级：班级3,姓名：学生I,年龄:11
```

班级数量、班级针对的学生明细、学生根据班级统计情况都很方便的统计出来了.

## FluentIterable

guava提供了FluentIterable对集合进行各种简化遍历的操作,这和Java8中的stream是很相似的.

例如对一个集合中的元素进行过滤

```java
public static void main(String[] args) {
    List<Student> list= Lists.newArrayList();
    list.add(new Student("学生A",12,"班级1"));
    list.add(new Student("学生B",13,"班级2"));
    list.add(new Student("学生C",12,"班级1"));
    list.add(new Student("学生D",15,"班级3"));
    list.add(new Student("学生E",12,"班级1"));
    list.add(new Student("学生F",13,"班级2"));
    list.add(new Student("学生G",11,"班级1"));
    list.add(new Student("学生H",15,"班级2"));
    list.add(new Student("学生I",11,"班级3"));
    list.add(new Student("学生J",12,"班级1"));
    list.add(new Student("学生W",16,"班级1"));
    list.add(new Student("学生Q",13,"班级4"));
    //arrmapTest(list);
    fluter(list);

}

static void fluter(List<Student> list){
    //过滤
    List<Student> studentList=FluentIterable.from(list).filter(new Predicate<Student>() {
        @Override
        public boolean apply(Student input) {
            return input.getClassRoom().equals("班级1");
        }
    }).toList();
    for (Student sd:studentList){
        System.out.println(sd.toString());
    }
}
```

此时,我们筛选班级1的学生，最终输出：

```java
班级：班级1,姓名：学生A,年龄:12
班级：班级1,姓名：学生C,年龄:12
班级：班级1,姓名：学生E,年龄:12
班级：班级1,姓名：学生G,年龄:11
班级：班级1,姓名：学生J,年龄:12
班级：班级1,姓名：学生W,年龄:16
```

通过Java8 来操作

```java
List<Student> li=list.stream().filter(student -> student.getClassRoom().equals("班级1")).collect(Collectors.toList());
for (Student sd:li){
    System.out.println(sd.toString());
}
```

## Optional

Optional操作是避免开发人员出现空指针操作而设计的,在Java8 中也有该对象的使用。