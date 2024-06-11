DROP TABLE IF EXISTS `catatan`;
DROP TABLE IF EXISTS `pengguna`;

CREATE TABLE `pengguna` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `nama` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `target_mulai` TIME NOT NULL,
    `target_selesai` TIME NOT NULL,
    `target_produktifitas` TIME NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`nama`)
);

CREATE TABLE `catatan` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `id_pengguna` INT UNSIGNED NOT NULL,
    `target_mulai` TIME NOT NULL,
    `target_selesai` TIME NOT NULL,
    `mulai` TIME NOT NULL,
    `selesai` TIME NOT NULL,
    `target_produktifitas` TIME NOT NULL,
    `tingkat_keproduktifan` DOUBLE,
    `tanggal` DATE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna`(`id`)
);
