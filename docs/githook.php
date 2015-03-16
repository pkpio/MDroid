<?php
  $data = file_get_contents('php://input');
  $json = json_decode($data);
  $head = $json->head_commit;
  
  $added = $head->added
  $removed = $head->removed
  $modified = $head->modified

  // Print added list
  foreach($added as $entry){
    echo $entry;
  }
?>
