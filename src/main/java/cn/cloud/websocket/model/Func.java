package cn.cloud.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Func {
    private Integer id;

    private String name;

    private String description;

    private String code;

    private String url;

    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Func func = (Func) o;
        return Objects.equals(id, func.id) &&
                Objects.equals(name, func.name) &&
                Objects.equals(description, func.description) &&
                Objects.equals(code, func.code) &&
                Objects.equals(url, func.url) &&
                Objects.equals(status, func.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, code, url, status);
    }
}