<?php
  error_reporting(E_ALL);
  $base_path = 'https://raw.githubusercontent.com/praveendath92/MDroid/master/';
  $interest_path = 'docs/';

  $data = file_get_contents('php://input');
  $json = json_decode($data);
  $head = $json->{'head_commit'};

  $added = $head->added;
  $removed = $head->removed;
  $modified = $head->modified;

  // Print added list
  foreach($added as $entry){
    if(startsWith($entry, $interest_path)){
      $file = fopen(substr($entry, 5), w);
      echo 'added ' . fwrite($file, file_get_contents($base_path . $entry));
      fclose($file);
    }
  }

  // Print removed list
  foreach($removed as $entry){
     if(startsWith($entry, $interest_path)){
        echo 'deleted ' . unlink(substr($entry, 5));
     }
  }

  // Print modified list
  foreach($modified as $entry){
    if(startsWith($entry, $interest_path)){
      $file = fopen(substr($entry, 5), w);
      echo 'modified ' . fwrite($file, file_get_contents($base_path . $entry));
      fclose($file);
    }
  }

  /**
   * Credit : SO
   */
  function startsWith($haystack, $needle) {
    return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== FALSE;
  }
?>
