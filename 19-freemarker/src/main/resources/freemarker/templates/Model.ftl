package ${packageName};
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ${className} {
<#list fields as attr>
    /**
    * ${attr.note}
    */
    private ${attr.type} ${attr.name};
</#list>

<#list fields as attr>
    public void set${attr.name?cap_first}(${attr.type} ${attr.name}){
        this.${attr.name} = ${attr.name};
    }

    public ${attr.type} get${attr.name?cap_first}(){
        return this.${attr.name};
    }

</#list>
}

