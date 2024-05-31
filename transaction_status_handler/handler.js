/* global fetch */

const EXPLORER_API = "https://api.ergoplatform.com";
const TRANSACTIONS = "/api/v1/transactions";
const TRANSACTIONS_UNCONFIRMED = "/transactions/unconfirmed";

export const handler = async(event, context, callback) => {
  const transactionId = event.transactionId;
  const unconfirmed = await isMempool(transactionId);
  const confirmed = await isConfirmed(transactionId);
  const response = {
    ...event,
    "transactionId": transactionId,
    "transactionStatus": unconfirmed ? "unconfirmed" : confirmed !== null ? "confirmed" : "not_found",
    "referenceBoxId": confirmed
  };
  callback(null, response);
};

const isMempool = async(transactionId) => {
  const response = await fetch(`${EXPLORER_API}${TRANSACTIONS_UNCONFIRMED}/${transactionId}`);
  return response.status !== 404;
};

const isConfirmed = async(transactionId) => {
  const response = await fetch(`${EXPLORER_API}${TRANSACTIONS}/${transactionId}`);
  const confirmed = response.status !== 404;
  if (confirmed) {
    const transaction = await response.json()
    const boxId = transaction["outputs"][0]["boxId"]
    return boxId
  } else {
    return null;
  }
};
