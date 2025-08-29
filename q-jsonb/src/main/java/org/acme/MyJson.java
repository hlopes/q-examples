package org.acme;

import java.util.List;
import java.util.Objects;

public class MyJson {
    private String value;
    private Element element;
    private List<Element> elements;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "MyJson{" +
            "value='" + value + '\'' +
            ", element=" + element +
            ", elements=" + elements +
            '}';
    }

    public static class Element {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Element element = (Element) o;
            return Objects.equals(code, element.code);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(code);
        }

        @Override
        public String toString() {
            return "Element{" +
                "code='" + code + '\'' +
                '}';
        }
    }
}
