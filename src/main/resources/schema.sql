create table if not exists sponsor(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    industry VARCHAR(255)

);

create table if not exists event(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    date VARCHAR(255)
);

create table if not exists event_sponsor(
    eventId INT,
    sponsorId INT,
    PRIMARY KEY(eventId,sponsorId),
    FOREIGN KEY(eventId) REFERENCES Event(id),
    FOREIGN KEY(sponsorId) REFERENCES Sponsor(id)

);