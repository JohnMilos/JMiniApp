---
sidebar_position: 8
---

# Creating Custom Adapters

Learn how to create custom format adapters for your specific data formats.

## Basic Custom Adapter

```java
public class MyAdapter implements JMiniFormatAdapter<MyModel> {
    @Override
    public String getFormatName() {
        return "myformat";
    }
    
    @Override
    public List<MyModel> read(InputStream input) throws IOException {
        // Deserialize from input stream
        return models;
    }
    
    @Override
    public void write(List<MyModel> data, OutputStream output) throws IOException {
        // Serialize to output stream
    }
}
```