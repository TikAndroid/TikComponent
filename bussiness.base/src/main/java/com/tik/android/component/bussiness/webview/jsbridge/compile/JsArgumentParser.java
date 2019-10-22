package com.tik.android.component.bussiness.webview.jsbridge.compile;

import android.text.TextUtils;

import com.tik.android.component.bussiness.webview.jsbridge.entity.JsArgumentType;
import com.tik.android.component.libcommon.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe : 获取 JS 的参数，并转化为 Java 的参数
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2018/11/20.
 */
public class JsArgumentParser {

    private long id;
    private String module;
    private String method;
    private List<Parameter> parameters;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public static class Parameter {
        @JsArgumentType.Type
        private int type;
        private String name;
        private String value;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static JsArgumentParser parse(String jsonString) {
        if(TextUtils.isEmpty(jsonString) || (!jsonString.startsWith("{") &&
                !jsonString.startsWith("["))) {
            return null;
        }
        JsArgumentParser parser = new JsArgumentParser();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            parser.setId(jsonObject.getLong("id"));
            parser.setMethod(jsonObject.getString("method"));
            parser.setModule(jsonObject.getString("module"));
            List<Parameter> parameterList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("parameters");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    if (item == null) {
                        continue;
                    }
                    Parameter parameter = new Parameter();
                    if (item.has("name")) {
                        parameter.setName(item.getString("name"));
                    }
                    if (item.has("type")) {
                        parameter.setType(item.getInt("type"));
                    }
                    if (item.has("value")) {
                        parameter.setValue(item.getString("value"));
                    }
                    parameterList.add(parameter);
                }
            }
            parser.setParameters(parameterList);
        } catch (Exception e) {
            LogUtil.e("JBArgumentParser::parse Exception", e);
        }
        return parser;
    }
}
