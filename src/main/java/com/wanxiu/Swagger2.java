package com.wanxiu;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class Swagger2 {


    @Bean
    public Docket createRestApi() {

        Predicate<RequestHandler> selector1=  RequestHandlerSelectors.basePackage("com.wanxiu.api");
        Predicate<RequestHandler> selector2 = RequestHandlerSelectors.basePackage("com.wanxiu.roomservice.controller");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(Predicates.or( selector1,selector2))
                .paths(PathSelectors.any())
                .build();
    }
    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("万秀直播")
                //创建人
                .contact(new Contact("吕明琪", "", "185148512@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("返回参数：/br 1.result:错误码,0 表示成功，非 0 表示失败/br 2.errmsg:错误消息，result 非 0 时的具体错误信息")
                .build();
    }

}
