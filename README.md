# Message格式

除Receive的Message之外，报头一行，报文一行，换行符用\r\n

对Receive的Message，除报头外剩下的每一行对应一个Publish的报文

报头：key:value词条构成，每个之间用空格分开

## Publish

Publish的Message报头：

Publish:queueName From:publisherName

Broker对Publish返回：

Status:success

## Subscribe

Subscribe的Message报头：

Subscribe:queueName From:subscriberName

Broker对Subscribe返回：

Status:success

## Receive

Receive的Message报头：

Receive:--ALL-- From:subscriberName

Broker对Receive返回：

Status:success Remaining:10

对Receive返回的报文中，开头都有一个From:publisherName，用空格与后面正式的内容分隔，如From:pb1 Trigger some events.

Receive:--ALL--表示从全部订阅的队列中获取消息，也就是实际上还可以指定某个队列来获取消息，不过这个先不做。

Remaining:n是在消息过多时提示Subscriber还有消息没传过来，让subscriber自己决策什么时候再次发动Receive