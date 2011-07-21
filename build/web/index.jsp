<jsp:useBean id="beatModel" scope="request" class="model.BeatModel" />
<html>
<head>
<title>DJ View</title>
</head>
<body>
<h1>DJ View</h1>
Beats per minutes = <jsp:getProperty name="beatModel" property="bpm" />
<br />
<hr>
<br />
<form method="post" action="index.jsp"> 
    BPM: <input type=text name="bpm" value="<jsp:getProperty name="beatModel" property="bpm"/>">

<input type="submit" name="set" value="set"><br />
<input type="submit" name="decrease" value="<<">
<input type="submit" name="increase" value=">>"><br />
<input type="submit" name="on" value="on">
<input type="submit" name="off" value="off"><br />
</form>
</body>
</html>