CREATE TABLE `tournament_player_data` (
  `obj_id` int(11) DEFAULT NULL,
  `fight_type` varchar(45) DEFAULT '',
  `fights_done` int(11) DEFAULT NULL,
  `victories` int(11) DEFAULT NULL,
  `defeats` int(11) DEFAULT NULL,
  `ties` int(11) DEFAULT NULL,
  `kills` int(11) DEFAULT NULL,
  `damage` int(11) DEFAULT NULL,
  `wdt` varchar(11) DEFAULT '',
  `dpf` varchar(11) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
