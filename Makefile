start:
	docker compose down
	docker compose up -d --wait

stop:
	docker compose down