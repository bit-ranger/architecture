<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>executionLog</title>
    <script src="/static/js/jquery-3.3.1.min.js"></script>
    <script src="/static/js/stomp.js"></script>
    <script src="/static/js/sockjs.min.js"></script>
</head>
<body onload="connect()">
<noscript><h2 style="color: #e80b0a;">Sorry，浏览器不支持WebSocket</h2></noscript>
<div  id="log-content" style="white-space: pre-line">

</div>
<script type="text/javascript">
    function connect() {
        var socket = new SockJS('/socket');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected:' + frame);
            stompClient.subscribe('/topic/execution/${executionId}/log', function (response) {
                showResponse(response.body);
            });
        });
    }

    function showResponse(message) {
        $("#log-content").append(message);
        var n = $(document).height();
        $('html, body').animate({ scrollTop: n }, 50);
    }
</script>
</body>
</html>