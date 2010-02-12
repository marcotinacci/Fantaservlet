SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `fsdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `fsdb`;

-- -----------------------------------------------------
-- Table `fsdb`.`Campionato`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Campionato` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Campionato` (
  `idCampionato` INT NOT NULL ,
  `Nome` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idCampionato`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Utente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Utente` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Utente` (
  `idUtente` INT NOT NULL ,
  `Nome` VARCHAR(45) NOT NULL ,
  `Password` VARCHAR(45) NOT NULL ,
  `Admin` TINYINT(1) NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`idUtente`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Squadra`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Squadra` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Squadra` (
  `idSquadra` INT NOT NULL ,
  `Campionato_idCampionato` INT NOT NULL ,
  `Utente_idUtente` INT NOT NULL ,
  `Nome` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idSquadra`) ,
  INDEX `fk_Squadra_Campionato` (`Campionato_idCampionato` ASC) ,
  INDEX `fk_Squadra_Utente1` (`Utente_idUtente` ASC) ,
  CONSTRAINT `fk_Squadra_Campionato`
    FOREIGN KEY (`Campionato_idCampionato` )
    REFERENCES `fsdb`.`Campionato` (`idCampionato` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Squadra_Utente1`
    FOREIGN KEY (`Utente_idUtente` )
    REFERENCES `fsdb`.`Utente` (`idUtente` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Giornata`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Giornata` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Giornata` (
  `idGiornata` INT NOT NULL ,
  `Campionato_idCampionato` INT NOT NULL ,
  `Data` DATE NOT NULL ,
  PRIMARY KEY (`idGiornata`) ,
  INDEX `fk_Giornata_Campionato1` (`Campionato_idCampionato` ASC) ,
  CONSTRAINT `fk_Giornata_Campionato1`
    FOREIGN KEY (`Campionato_idCampionato` )
    REFERENCES `fsdb`.`Campionato` (`idCampionato` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Voto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Voto` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Voto` (
  `idVoto` INT NOT NULL ,
  `Azione` VARCHAR(45) NOT NULL ,
  `Punteggio` INT NOT NULL ,
  PRIMARY KEY (`idVoto`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Calciatore`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Calciatore` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Calciatore` (
  `idCalciatore` INT NOT NULL ,
  `Nome` VARCHAR(45) NOT NULL ,
  `Ruolo` CHAR(1) NOT NULL ,
  `Squadra` VARCHAR(45) NULL ,
  PRIMARY KEY (`idCalciatore`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Pagella`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Pagella` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Pagella` (
  `idPagella` INT NOT NULL ,
  `Voto_idVoto` INT NOT NULL ,
  `Giornata_idGiornata` INT NOT NULL ,
  `Calciatore_idCalciatore` INT NOT NULL ,
  PRIMARY KEY (`idPagella`) ,
  INDEX `fk_Pagella_Voto1` (`Voto_idVoto` ASC) ,
  INDEX `fk_Pagella_Giornata1` (`Giornata_idGiornata` ASC) ,
  INDEX `fk_Pagella_Calciatore1` (`Calciatore_idCalciatore` ASC) ,
  CONSTRAINT `fk_Pagella_Voto1`
    FOREIGN KEY (`Voto_idVoto` )
    REFERENCES `fsdb`.`Voto` (`idVoto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pagella_Giornata1`
    FOREIGN KEY (`Giornata_idGiornata` )
    REFERENCES `fsdb`.`Giornata` (`idGiornata` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pagella_Calciatore1`
    FOREIGN KEY (`Calciatore_idCalciatore` )
    REFERENCES `fsdb`.`Calciatore` (`idCalciatore` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Convocazione`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Convocazione` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Convocazione` (
  `idConvocazione` INT NOT NULL ,
  `Squadra_idSquadra` INT NOT NULL ,
  `Calciatore_idCalciatore` INT NOT NULL ,
  PRIMARY KEY (`idConvocazione`) ,
  INDEX `fk_Convocazione_Squadra1` (`Squadra_idSquadra` ASC) ,
  INDEX `fk_Convocazione_Calciatore1` (`Calciatore_idCalciatore` ASC) ,
  CONSTRAINT `fk_Convocazione_Squadra1`
    FOREIGN KEY (`Squadra_idSquadra` )
    REFERENCES `fsdb`.`Squadra` (`idSquadra` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Convocazione_Calciatore1`
    FOREIGN KEY (`Calciatore_idCalciatore` )
    REFERENCES `fsdb`.`Calciatore` (`idCalciatore` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Schieramento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Schieramento` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Schieramento` (
  `idSchieramento` INT NOT NULL ,
  `Convocazione_idConvocazione` INT NOT NULL ,
  `Giornata_idGiornata` INT NOT NULL ,
  PRIMARY KEY (`idSchieramento`) ,
  INDEX `fk_Schieramento_Convocazione1` (`Convocazione_idConvocazione` ASC) ,
  INDEX `fk_Schieramento_Giornata1` (`Giornata_idGiornata` ASC) ,
  CONSTRAINT `fk_Schieramento_Convocazione1`
    FOREIGN KEY (`Convocazione_idConvocazione` )
    REFERENCES `fsdb`.`Convocazione` (`idConvocazione` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schieramento_Giornata1`
    FOREIGN KEY (`Giornata_idGiornata` )
    REFERENCES `fsdb`.`Giornata` (`idGiornata` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fsdb`.`Partita`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fsdb`.`Partita` ;

CREATE  TABLE IF NOT EXISTS `fsdb`.`Partita` (
  `idPartita` INT NOT NULL ,
  `Squadra_idSquadra1` INT NOT NULL ,
  `Squadra_idSquadra2` INT NOT NULL ,
  `Giornata_idGiornata` INT NOT NULL ,
  PRIMARY KEY (`idPartita`) ,
  INDEX `fk_Partita_Squadra1` (`Squadra_idSquadra1` ASC) ,
  INDEX `fk_Partita_Squadra2` (`Squadra_idSquadra2` ASC) ,
  INDEX `fk_Partita_Giornata1` (`Giornata_idGiornata` ASC) ,
  CONSTRAINT `fk_Partita_Squadra1`
    FOREIGN KEY (`Squadra_idSquadra1` )
    REFERENCES `fsdb`.`Squadra` (`idSquadra` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Partita_Squadra2`
    FOREIGN KEY (`Squadra_idSquadra2` )
    REFERENCES `fsdb`.`Squadra` (`idSquadra` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Partita_Giornata1`
    FOREIGN KEY (`Giornata_idGiornata` )
    REFERENCES `fsdb`.`Giornata` (`idGiornata` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
