### Create the file 
sudo nano /etc/systemd/system/docker-compose-app.service

### Content of the file:
```
[Unit]
Description=Docker Compose Application Service
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/srv
ExecStart=/usr/local/bin/docker-compose up -d
ExecStop=/usr/local/bin/docker-compose down
TimeoutStartSec=5

[Install]
WantedBy=multi-user.target
```

`WorkingDirectory` property must have the path for the `docker-compose.yaml` file

### Enable Service
systemctl enable docker-compose-app.service
systemctl start docker-compose-app.service