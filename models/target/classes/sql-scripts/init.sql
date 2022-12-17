INSERT INTO vrsta(vrsta) VALUES ('Macbook Pro M1 Pro 512 GB');
INSERT INTO vrsta(vrsta) VALUES ('iPhone 14 Pro Max 256 GB');
INSERT INTO trgovina(ime, ustanovitev, sedez) VALUES ('iStyle', '2012-01-01', 'Slovenia');
INSERT INTO trgovina(ime, ustanovitev, sedez) VALUES ('Epl', '2013-05-18', 'Slovenia');
INSERT INTO izdelek(vrsta_id, trgovina_id, ime, cena, zadnja_sprememba) VALUES ('1', '1', 'iStyle Macbook', '249900', '2022-10-26');
INSERT INTO izdelek(vrsta_id, trgovina_id, ime, cena, zadnja_sprememba) VALUES ('2', '2', 'Epl iPhone 14 Pro Max', '179900', '2022-10-26');

INSERT INTO kosarica(kosarica_id, ime) VALUES ('1', 'Apple Kosarica');

INSERT INTO kosarica_izdelek(kosaricaentity_kosarica_id, izdelki_izdelek_id) VALUES ('1', '1');