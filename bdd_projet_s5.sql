-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mar. 07 jan. 2020 à 16:26
-- Version du serveur :  10.4.10-MariaDB
-- Version de PHP :  7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `bdd_projet_s5`
--

-- --------------------------------------------------------

--
-- Structure de la table `appartenirud`
--

DROP TABLE IF EXISTS `appartenirud`;
CREATE TABLE IF NOT EXISTS `appartenirud` (
  `IdU` int(11) NOT NULL,
  `IdD` int(11) NOT NULL,
  PRIMARY KEY (`IdU`,`IdD`),
  KEY `FOREIGN_APP2_IDD` (`IdD`),
  KEY `FOREIGN_APP2_IDU` (`IdU`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `appartenirug`
--

DROP TABLE IF EXISTS `appartenirug`;
CREATE TABLE IF NOT EXISTS `appartenirug` (
  `IdU` int(11) NOT NULL,
  `IdG` int(11) NOT NULL,
  PRIMARY KEY (`IdU`,`IdG`),
  KEY `FOREIGN_APP1_IDU` (`IdU`),
  KEY `FOREIGN_APP1_IDG` (`IdG`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `discussion`
--

DROP TABLE IF EXISTS `discussion`;
CREATE TABLE IF NOT EXISTS `discussion` (
  `IdD` int(11) NOT NULL,
  `NbD` int(11) NOT NULL DEFAULT 0,
  `Name` varchar(30) NOT NULL,
  PRIMARY KEY (`IdD`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `groupe`
--

DROP TABLE IF EXISTS `groupe`;
CREATE TABLE IF NOT EXISTS `groupe` (
  `IdG` int(11) NOT NULL,
  `NbG` int(11) NOT NULL DEFAULT 0,
  `Name` varchar(30) NOT NULL,
  PRIMARY KEY (`IdG`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `IdM` int(11) NOT NULL,
  `NbM` int(11) NOT NULL DEFAULT 0,
  `Content` varchar(3000) NOT NULL,
  `IsRead` int(1) NOT NULL,
  `Time` varchar(50) NOT NULL,
  `NbVu` int(11) DEFAULT 0,
  `IdU` int(11) NOT NULL,
  `IdD` int(11) NOT NULL,
  PRIMARY KEY (`IdM`),
  KEY `IdU` (`IdU`),
  KEY `IdD` (`IdD`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `IdU` int(11) NOT NULL,
  `NbU` int(11) NOT NULL DEFAULT 0,
  `Name` varchar(20) NOT NULL,
  `ForName` varchar(20) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `IsConnected` int(11) NOT NULL,
  PRIMARY KEY (`IdU`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `appartenirud`
--
ALTER TABLE `appartenirud`
  ADD CONSTRAINT `FOREIGN_APP2_IDD` FOREIGN KEY (`IdD`) REFERENCES `discussion` (`IdD`),
  ADD CONSTRAINT `FOREIGN_APP2_IDU` FOREIGN KEY (`IdU`) REFERENCES `user` (`IdU`);

--
-- Contraintes pour la table `appartenirug`
--
ALTER TABLE `appartenirug`
  ADD CONSTRAINT `FOREIGN_APP1_IDG` FOREIGN KEY (`IdG`) REFERENCES `groupe` (`IdG`),
  ADD CONSTRAINT `FOREIGN_APP1_IDU` FOREIGN KEY (`IdU`) REFERENCES `user` (`IdU`);

--
-- Contraintes pour la table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FOREIGN_IdD` FOREIGN KEY (`IdD`) REFERENCES `discussion` (`IdD`),
  ADD CONSTRAINT `FOREIGN_IdU` FOREIGN KEY (`IdU`) REFERENCES `user` (`IdU`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
