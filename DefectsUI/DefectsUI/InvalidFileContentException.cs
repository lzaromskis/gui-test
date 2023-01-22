using System.Runtime.Serialization;

namespace DefectsUI
{
    [Serializable]
    internal class InvalidFileContentException : Exception
    {
        public InvalidFileContentException()
        {
        }

        public InvalidFileContentException(string? message) : base(message)
        {
        }

        public InvalidFileContentException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected InvalidFileContentException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}