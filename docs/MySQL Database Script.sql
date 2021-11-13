-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema storeappointmentsystem
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema storeappointmentsystem
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `storeappointmentsystem` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `storeappointmentsystem` ;

-- -----------------------------------------------------
-- Table `storeappointmentsystem`.`shop`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storeappointmentsystem`.`shop` (
  `id` INT NOT NULL,
  `name` VARCHAR(250) NULL DEFAULT NULL,
  `address` VARCHAR(250) NULL DEFAULT NULL,
  `phone` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storeappointmentsystem`.`appointment_inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storeappointmentsystem`.`appointment_inventory` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(250) NULL DEFAULT NULL,
  `store_id` INT NULL DEFAULT NULL,
  `description` VARCHAR(250) NULL DEFAULT NULL,
  `active` INT NULL DEFAULT NULL,
  `start_time` DATETIME NULL DEFAULT NULL,
  `end_time` DATETIME NULL DEFAULT NULL,
  `total_spot` INT NULL DEFAULT NULL,
  `available_spot` INT NULL DEFAULT NULL,
  `locked_spot` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `appointment_inventory_to_store_storeid_idx` (`store_id` ASC) VISIBLE,
  CONSTRAINT `appointment_inventory_to_store_storeid`
    FOREIGN KEY (`store_id`)
    REFERENCES `storeappointmentsystem`.`shop` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storeappointmentsystem`.`shopper`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storeappointmentsystem`.`shopper` (
  `id` INT NOT NULL,
  `name` VARCHAR(250) NULL DEFAULT NULL,
  `address` VARCHAR(250) NULL DEFAULT NULL,
  `phone` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storeappointmentsystem`.`appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storeappointmentsystem`.`appointment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `appointment_no` VARCHAR(100) NULL DEFAULT NULL,
  `status` INT NULL DEFAULT NULL,
  `appointment_inventory_id` INT NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  `appointment_count` INT NULL DEFAULT NULL,
  `create_time` DATETIME NULL DEFAULT NULL,
  `confirm_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `appointment_to_user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `appointment_to_inventory_Id_idx` (`appointment_inventory_id` ASC) VISIBLE,
  CONSTRAINT `appointment_to_inventory_Id`
    FOREIGN KEY (`appointment_inventory_id`)
    REFERENCES `storeappointmentsystem`.`appointment_inventory` (`id`),
  CONSTRAINT `appointment_to_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `storeappointmentsystem`.`shopper` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3096
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
