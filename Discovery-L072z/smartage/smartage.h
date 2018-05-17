#ifndef __SX1276PINGPONG_H__
#define __SX1276PINGPONG_H__

#ifdef FEATURE_LORA
void SendAndBack(uint8_t* str);
void print_stuff();
#else
#define  SendAndBack(x)   void()
#endif
/*
 * Callback functions prototypes
 */
/*!
 * @brief Function to be executed on Radio Tx Done event
 */
void OnTxDone(void *radio, void *userThisPtr, void *userData);

/*!
 * @brief Function to be executed on Radio Rx Done event
 */
void OnRxDone(void *radio, void *userThisPtr, void *userData, uint8_t *payload, uint16_t size, int16_t rssi, int8_t snr );

/*!
 * @brief Function executed on Radio Tx Timeout event
 */
void OnTxTimeout(void *radio, void *userThisPtr, void *userData);

/*!
 * @brief Function executed on Radio Rx Timeout event
 */
void OnRxTimeout(void *radio, void *userThisPtr, void *userData);

/*!
 * @brief Function executed on Radio Rx Error event
 */
void OnRxError(void *radio, void *userThisPtr, void *userData);

/*!
 * @brief Function executed on Radio Fhss Change Channel event
 */
void OnFhssChangeChannel(void *radio, void *userThisPtr, void *userData, uint8_t channelIndex);

/*!
 * @brief Function executed on CAD Done event
 */
void OnCadDone(void *radio, void *userThisPtr, void *userData);

#endif // __MAIN_H__