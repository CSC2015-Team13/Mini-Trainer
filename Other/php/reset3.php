<?php
   $username = $_GET['username'];
   $token = $_GET['token'];
   ?>
<html>
<head>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.1.1.min.js"></script>
<style>
   body{text-align:center;}

.clear {clear:both}

.block {
   width:400px;
   margin:0 auto;
   text-align:left;
}
.element * {
   padding:5px; 
   margin:2px; 
   font-family:arial;
   font-size:12px;
}
.element label {
   float:left; 
   width:75px;
   font-weight:700
}
.element input.text {
   float:left; 
   width:270px;
   padding-left:20px;
}
.element .textarea {
   height:120px; 
   width:270px;
   padding-left:20px;
}
.element .hightlight {
   border:2px solid #9F1319;
   background:url(iconCaution.gif) no-repeat 2px
}
.element #submit {
   float:right;
   margin-right:10px;
}
.loading {
   float:right; 
   background:url(ajax-loader.gif) no-repeat 1px; 
   height:28px; 
   width:28px; 
   display:none;
}
.done {
   background:url(iconIdea.gif) no-repeat 2px; 
   padding-left:20px;
   font-family:arial;
   font-size:12px; 
   width:70%; 
   margin:20px auto; 
   display:none
}
.error {
   background:url(iconIdea.gif) no-repeat 2px; 
   padding-left:20px;
   font-family:arial;
   font-size:12px; 
   width:70%; 
   margin:20px auto; 
   display:none
}
</style>
<script>
$(document).ready(function () {
   $('#submit').click(function ()
   {
      var username = $('input[name=username]');
      var pass1 = $('input[name=pass1]');
      var pass2 = $('input[name=pass2]');
      var token = $('input[name=token]');

      var data = 'username=' + username.val() + '&token=' + token.val() + '&pass1=' + pass1.val() + '&pass2=' + pass2.val();

      $('.text').attr('disabled','true');
      $('.loading').show();
      $.ajax({
         url: "ajax_reset_functions.php",
         type: "GET",
         data: data,
         cache: false,
         success: function (html) {
            if(html==1) {
               $('.form').fadeOut('slow');
               $('.done').fadeIn('slow');
            }
            else {
               alert(html);
            }
            $('.loading').hide();
            $('.text').removeAttr('disabled');
         }
      });
         return false;
   });
})
</script>

</head>

   <div class="block">
<div class="done">
<b>Your password has been successfully changed!</b> 
</div>
<div class="error">

</div>
   <div class="form">
   <form>
   <div class="element">
      <label>Password</label>
      <input type="password" name="pass1" class="text" />
   </div>
   <div class="element">
      <label>Retype Password</label>
      <input type="password" name="pass2" class="text" />
   </div>
      <input type="hidden" value="<?php echo $username;?>" name="username" />
      <input type="hidden" value="<?php echo $token;?>" name="token" />
   <div class="element">
      
      <input type="submit" id="submit"/>
      <div class="loading"></div>
   </div>
   </form>
   </div>
</div>
<div class="clear"></div>
</html>