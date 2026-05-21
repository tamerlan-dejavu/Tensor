export type Mode = 'encrypt' | 'decrypt'

export interface CipherRequest {
  text: string
  key: string
}

export interface CipherResponse {
  result: string
  operation: string
  processingTimeMs: number
}

export interface ErrorResponse {
  error: string
  status: number
  message: string
}
