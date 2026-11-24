#!/usr/bin/env python3
"""
PrintShop Customer Manager - Firebase Cloud Firestore Seed Script
----------------------------------------------------------------
This script populates Cloud Firestore with 10 realistic dummy printing shop customers.
It uses deterministic IDs (seed_cust_001 .. seed_cust_010) to guarantee idempotency.

Requirements:
    pip install firebase-admin

Usage:
    python seed_firestore.py --service-account path/to/serviceAccountKey.json
"""

import os
import sys
import json
import argparse

try:
    import firebase_admin
    from firebase_admin import credentials, firestore
except ImportError:
    print("[ERROR] firebase-admin is not installed. Please run: pip install firebase-admin")
    sys.exit(1)


def seed_customers(service_account_path, json_data_path="dummy_customers.json"):
    if not os.path.exists(service_account_path):
        print(f"[ERROR] Service account file not found: {service_account_path}")
        sys.exit(1)

    if not os.path.exists(json_data_path):
        # Look in the script directory
        script_dir = os.path.dirname(os.path.abspath(__file__))
        json_data_path = os.path.join(script_dir, "dummy_customers.json")

    print(f"[INFO] Initializing Firebase Admin SDK with {service_account_path}...")
    cred = credentials.Certificate(service_account_path)
    firebase_admin.initialize_app(cred)
    db = firestore.client()

    print(f"[INFO] Loading dummy customers from {json_data_path}...")
    with open(json_data_path, "r", encoding="utf-8") as f:
        customers = json.load(f)

    batch = db.batch()
    collection_ref = db.collection("customers")

    print(f"[INFO] Seeding {len(customers)} customers into collection 'customers'...")
    for item in customers:
        doc_id = item.get("customerId")
        doc_ref = collection_ref.document(doc_id)
        # Exclude customerId from document payload to match Firestore pattern
        data = {k: v for k, v in item.items() if k != "customerId"}
        batch.set(doc_ref, data)
        print(f"  + Queued: {item['fullName']} ({doc_id})")

    batch.commit()
    print("[SUCCESS] Successfully seeded 10 customers into Cloud Firestore without duplicates!")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Seed Cloud Firestore with PrintShop dummy customer records")
    parser.add_argument("--service-account", required=True, help="Path to Firebase serviceAccountKey.json")
    parser.add_argument("--json", default="dummy_customers.json", help="Path to dummy customers JSON file")
    args = parser.parse_args()

    seed_customers(args.service_account, args.json)
