Попробовал использовать 
```java
private ObjectOutputStream objectOutputStream;
private ObjectInputStream objectInputStream;
```
и передавать объект **Message** 

```java
public class Message implements Externalizable {
    private User senderClient;
    private List<String> privateGroupNames;
    private LocalDateTime time;
    private String data;
    private boolean systemMessage;

    public Message() {
    }

    public Message(User senderClient, boolean systemMessage, List<String> privateGroup, LocalDateTime time, String data) {
        this.senderClient = senderClient;
        this.systemMessage = systemMessage;
        this.privateGroupNames = privateGroup;
        this.time = time;
        this.data = data;
    }
    .....
```

Личные сообщения реализованы так: 
```
@list - список подключенных к чату
@only + @имя пользователя (можно несколько) - личное сообщение
```
