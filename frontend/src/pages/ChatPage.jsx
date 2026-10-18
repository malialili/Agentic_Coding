import React, { useState, useRef, useEffect } from 'react';
import { Box, Heading, VStack, Spinner, Alert, AlertIcon, useColorModeValue } from '@chakra-ui/react';
import ChatMessage from '../components/ChatMessage';
import ChatInput from '../components/ChatInput';
import { agentService } from '../services/api';

const ChatPage = () => {
  const [messages, setMessages] = useState([
    {
      id: 1,
      text: "Bonjour ! Je suis Agentic AI, votre assistant intelligent. Comment puis-je vous aider aujourd'hui ?",
      isUser: false,
      timestamp: new Date(),
    },
  ]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [sessionId, setSessionId] = useState(localStorage.getItem('sessionId') || null);
  const messagesEndRef = useRef(null);

  const bgColor = useColorModeValue('white', 'gray.800');

  // Scroll automatique vers le bas
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  // Sauvegarder le sessionId dans le localStorage
  useEffect(() => {
    if (sessionId) {
      localStorage.setItem('sessionId', sessionId);
    }
  }, [sessionId]);

  const handleSend = async (message) => {
    if (!message.trim()) return;

    setError(null);
    setIsLoading(true);

    // Ajouter le message de l'utilisateur
    const userMessage = {
      id: messages.length + 1,
      text: message,
      isUser: true,
      timestamp: new Date(),
    };
    setMessages((prev) => [...prev, userMessage]);

    try {
      // Appeler l'API de l'agent
      const response = await agentService.askQuestion(message, sessionId);
      
      // Générer un nouvel ID de session si ce n'est pas déjà fait
      const newSessionId = response.data.sessionId || sessionId;
      if (newSessionId && !sessionId) {
        setSessionId(newSessionId);
      }

      // Ajouter la réponse de l'agent
      const agentMessage = {
        id: messages.length + 2,
        text: response.data.answer || "Désolé, je n'ai pas pu générer de réponse.",
        isUser: false,
        timestamp: new Date(),
        sources: response.data.sources,
      };
      setMessages((prev) => [...prev, agentMessage]);

    } catch (err) {
      console.error('Erreur lors de l\'envoi du message:', err);
      setError(err.message || "Une erreur est survenue lors de l'envoi de votre message.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleClearChat = () => {
    setMessages([
      {
        id: 1,
        text: "Bonjour ! Je suis Agentic AI, votre assistant intelligent. Comment puis-je vous aider aujourd'hui ?",
        isUser: false,
        timestamp: new Date(),
      },
    ]);
    setSessionId(null);
    localStorage.removeItem('sessionId');
  };

  return (
    <Box h="full" display="flex" flexDirection="column" bg={bgColor} borderRadius="lg" boxShadow="sm">
      {/* En-tête */}
      <Box p={4} borderBottom="1px" borderBottomColor="gray.200">
        <Heading as="h2" size="lg" color="gray.800">
          Chat avec Agentic AI
        </Heading>
      </Box>

      {/* Zone des messages */}
      <VStack
        flex={1}
        p={4}
        spacing={4}
        overflowY="auto"
        align="stretch"
      >
        {messages.length === 0 ? (
          <Box textAlign="center" py={10}>
            <Heading as="h3" size="md" color="gray.500" mb={4}>
              Commencez une conversation
            </Heading>
            <Box color="gray.400">
              Posez une question pour démarrer l'interaction avec l'agent IA.
            </Box>
          </Box>
        ) : (
          messages.map((msg) => (
            <ChatMessage
              key={msg.id}
              message={msg.text}
              isUser={msg.isUser}
            />
          ))
        )}
        
        {/* Indicateurs de chargement et d'erreur */}
        {isLoading && (
          <Box display="flex" justifyContent="center" py={4}>
            <Spinner size="xl" color="primary.500" />
          </Box>
        )}

        {error && (
          <Alert status="error" borderRadius="lg">
            <AlertIcon />
            {error}
          </Alert>
        )}

        <div ref={messagesEndRef} />
      </VStack>

      {/* Barre d'entrée */}
      <ChatInput onSend={handleSend} isLoading={isLoading} />

      {/* Bouton pour effacer le chat */}
      {messages.length > 1 && (
        <Box px={4} pb={4} textAlign="center">
          <Box
            as="button"
            onClick={handleClearChat}
            color="gray.500"
            fontSize="sm"
            _hover={{ color: 'red.500', textDecoration: 'underline' }}
            cursor="pointer"
          >
            Effacer la conversation
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default ChatPage;
