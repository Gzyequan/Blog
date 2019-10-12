<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>index Page</title>
    <script type="text/javascript">
        var websocket = null;

        function initWebsocket(){
            var userId = document.getElementById('userId').value;
            //判断当前浏览器是否支持WebSocket
            if ('WebSocket' in window) {
                websocket = new WebSocket("ws://127.0.0.1:8069/commonweb/websocket/"+userId);
                //连接发生错误的回调方法
                websocket.onerror = function () {
                    setMessageInnerHTML("WebSocket连接发生错误");
                };

                //连接成功建立的回调方法
                websocket.onopen = function () {
                    setMessageInnerHTML("WebSocket连接成功");
                };

                //接收到消息的回调方法
                websocket.onmessage = function (event) {
                    setMessageInnerHTML(event.data);
                };

                //连接关闭的回调方法
                websocket.onclose = function () {
                    setMessageInnerHTML("WebSocket连接关闭");
                };

                //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
                window.onbeforeunload = function () {
                    closeWebSocket();
                };
            } else {
                alert('当前浏览器 Not support websocket')
            }
        }

        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML) {
            var msg = innerHTML.split(" - ")

            var table = document.getElementById("tb");

            var row;
            row = table.insertRow(1);
            for (var i = 0; i < msg.length; i++) {
                var cell = row.insertCell(i);
                cell.appendChild(document.createTextNode(msg[i]));
            }
            if (table.rows.length > 50) {
                table.deleteRow(table.rows.length - 1);
            }
            //  document.getElementById('message').innerHTML += innerHTML + '<br/>';
        }

        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
        }

        //发送消息
        function send() {
            var message = document.getElementById('text').value;
            websocket.send(message);
        }
    </script>
</head>
<body>

Welcome<br/>
<input id="userId" type="text"/>
<button onclick="initWebsocket()">建立连接</button>
<hr/>

<input id="text" type="text"/>
<button onclick="send()">发送消息</button>
<hr/>

<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<hr/>
<div id="message"></div>
<table id="tb" class="altrowstable">
    <th align="center" colspan="9">实时信息监控</th>
</table>
</body>


</html>
