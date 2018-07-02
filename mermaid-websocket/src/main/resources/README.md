# mermaid websocket introduce
mermaid websocket is single node sever on version 1.0
## quick start
websocket模块使用声明式和编程式实现了websocket server
编程式使用了netty 来实现
## 关于配置
配置文件resources/META-INF/mermaid-module-websocket.propertes
### 配置说明
| 配置项 | 默认值 | 说明 |

|mermaid.websocket.connect.welcome | welcom to use mermaid websocket! | websocketk连接成功提示语 |
|mermaid.websocket.disconnect | disconnected succes| 断开连接提示 |
|mermaid.websocket.clientId | userId | session attribute的key |
## 关于消息文本说明
消息必须以json格式发送
json格式如下：
```json
{
  "transfer":false,
  "recivedUserIds":["0001","0002"],
  "msgType":"EnumMessageType",
  "message":"messageContent"
}
```