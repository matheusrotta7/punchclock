sudo kill $(sudo jcmd | awk  '/punch/ {print $1}')
