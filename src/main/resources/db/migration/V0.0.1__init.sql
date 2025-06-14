-- Table for storing chat history
CREATE TABLE IF NOT EXISTS chat_history (
    id BIGINT PRIMARY KEY,
    message_text TEXT NOT NULL,
    is_user_message BOOLEAN NOT NULL,  -- TRUE if sent by user, FALSE if system response
    timestamp TEXT DEFAULT (datetime('now','localtime'))
);

-- Table for storing documents in the conversation
CREATE TABLE IF NOT EXISTS chat_documents (
    id BIGINT PRIMARY KEY,
    chat_message_id INTEGER,           -- Reference to the message this document is attached to
    document_name TEXT NOT NULL,       -- Original filename
    document_type TEXT NOT NULL,       -- MIME type or simple type (pdf, audio, text)
    document_data BLOB NOT NULL,       -- Binary content of the document
    document_size INTEGER NOT NULL,    -- Size in bytes
    upload_timestamp TEXT DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (chat_message_id) REFERENCES chat_history(id)
);

-- Optional: Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_chat_documents_message_id ON chat_documents(chat_message_id);