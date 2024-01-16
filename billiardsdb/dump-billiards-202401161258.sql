-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: billiards
-- ------------------------------------------------------
-- Server version	5.5.5-10.11.5-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `federation_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `game_type` varchar(20) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `published` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `CompetionFederationId` (`federation_id`) USING BTREE,
  CONSTRAINT `competition-federation_id` FOREIGN KEY (`federation_id`) REFERENCES `federation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competition`
--

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;
INSERT INTO `competition` VALUES (1,1,'De Zilveren Cup','ONE_CUSHION','2023-10-25','2023-12-10',_binary ''),(2,1,'Joost Janssen Beker','ONE_CUSHION','2023-06-20','2023-11-16',_binary '\0'),(3,1,'Metal Masters','THREE_CUSHION','2023-02-01','2023-06-10',_binary ''),(4,1,'Libre 1','ONE_CUSHION','2024-07-16','2024-12-16',_binary ''),(6,1,'Newbies','BALKLINE','2023-01-01','2023-12-31',_binary ''),(7,1,'Metal Masters','THREE_CUSHION','2023-02-01','2023-06-10',_binary ''),(16,1,'Metal Masters','THREE_CUSHION','2023-02-01','2023-06-10',_binary '\0'),(35,1,'DaCompas','STRAIGHT_RAIL','2023-11-01','2024-06-10',_binary ''),(43,1,'EGame','STRAIGHT_RAIL','2023-10-18','2024-06-10',_binary '\0'),(45,1,'De grootte Club Cub','ONE_CUSHION','2023-11-07','2024-11-21',_binary '\0'),(46,1,'De kappers','THREE_CUSHION','2023-11-11','2025-06-10',_binary '\0'),(47,1,'BassieBanden','THREE_CUSHION','2023-11-10','2024-11-10',_binary '\0'),(60,1,'test5','BALKLINE','2023-11-04','2023-11-18',_binary '\0'),(61,1,'UsabilityTestComp','STRAIGHT_RAIL','2023-11-14','2023-11-14',_binary '\0');
/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `federation`
--

DROP TABLE IF EXISTS `federation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `federation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `federation`
--

LOCK TABLES `federation` WRITE;
/*!40000 ALTER TABLE `federation` DISABLE KEYS */;
INSERT INTO `federation` VALUES (1,'Dongense Biljart Bond');
/*!40000 ALTER TABLE `federation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'Lars van Delft'),(2,'Robin van der Haak'),(3,'Geert van Vught'),(4,'Jos Michielsen'),(5,'Chantal Michielsen'),(6,'Kris Haast');
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competition_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `home_game_day` varchar(10) NOT NULL,
  `times_viewed` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team-competition_id` (`competition_id`),
  CONSTRAINT `team-competition_id` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,1,'A.L.M.','MONDAY',1),(2,1,'De Viersprong 4','MONDAY',0),(3,1,'KenNet','MONDAY',1),(4,1,'O.N.A. 3','MONDAY',0),(5,2,'Euphonia 3','MONDAY',1),(6,2,'De Bobo\'s','TUESDAY',5),(7,2,'De Viersprong 3','MONDAY',0),(8,3,'De Keu','MONDAY',0),(9,3,'\'t Vaartje 2','MONDAY',0),(10,2,'Excelsior','MONDAY',4),(19,1,'A.L.M.2','MONDAY',0),(25,61,'Team de kloppers','MONDAY',0),(26,61,'Team de banjos','MONDAY',0),(27,45,'asefsefsef','MONDAY',0),(28,45,'Jaja2','MONDAY',2),(32,35,'Taylor','MONDAY',0),(33,35,'sesse','TUESDAY',0),(34,35,'sessesss','TUESDAY',286),(35,35,'bsdfsefsef','TUESDAY',0),(36,35,'YYSYSYS','MONDAY',18),(37,35,'Testersss','MONDAY',0);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_player`
--

DROP TABLE IF EXISTS `team_player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_player` (
  `team_id` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  PRIMARY KEY (`team_id`,`player_id`) USING BTREE,
  KEY `team_player-player_id` (`player_id`),
  CONSTRAINT `team_player-player_id` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `team_player-team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_player`
--

LOCK TABLES `team_player` WRITE;
/*!40000 ALTER TABLE `team_player` DISABLE KEYS */;
INSERT INTO `team_player` VALUES (3,1),(5,6),(6,1),(6,4),(19,1),(19,2),(25,3),(25,4),(26,3),(26,6),(28,3),(28,6),(32,2),(32,5),(33,3),(33,6),(34,3),(34,4),(35,2),(35,6),(36,4),(37,6);
/*!40000 ALTER TABLE `team_player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'billiards'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-16 12:58:30
