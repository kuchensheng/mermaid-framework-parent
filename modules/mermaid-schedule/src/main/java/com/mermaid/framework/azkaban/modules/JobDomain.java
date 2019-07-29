package com.mermaid.framework.azkaban.modules;

/**
 * ClassName:JobDomain
 * Description: TODO
 *
 * @author: kuchensheng
 * @version: Create at:  10:37
 * _
 * Copyright:   Copyright (c)2019
 * Company:     songxiaocai
 * _
 * Modification History:
 * Date              Author      Version     Description
 * ------------------------------------------------------------------
 * 10:37   kuchensheng    1.0
 */

public class JobDomain {

    private JobTypeEnum jobType;

    private String command;


    enum JobTypeEnum {
        COMMAND("command"),JAVA("java");
        private String value;

        JobTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
